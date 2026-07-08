# AlgoForge IDE

A web IDE for practicing the algorithm solutions in this repository. It lists every
algorithm file (grouped into **Categories** and **Companies**), shows the chosen file's
content in a read-only pane, gives you an editable pane to practice, and runs your code in
a **server-side sandbox** — comparing your program's output against the standard solution.

## Architecture

```
Browser (React + Monaco)
   │  /api/tree, /api/file, /api/run
   ▼
Express API (server/index.mjs)  ──►  Piston sandbox
   │                                    compiles + runs untrusted code
   └── reads algorithm files from disk  (namespaces + prlimit, cgroup v2)
```

- **File listing & content** come from the Express server (`server/index.mjs`), which
  reads the repo folders from disk and blocks path traversal outside the repo root.
- **Code execution is server-side.** The API forwards `{ source, language, stdin }` to a
  [Piston](https://github.com/engineer-man/piston) instance that compiles and runs the
  code in an isolated sandbox (Linux namespaces + `prlimit`), then returns
  stdout/stderr, exit status, and signals. This is the same model LeetCode uses, and it
  lets us run **Java, Python, JavaScript, TypeScript, C++, C, and Go** — not just browser
  languages.
- **Why Piston (not Judge0):** Piston runs cleanly on **cgroup v2**, so it works on modern
  kernels (Docker Desktop / WSL2, and current Azure Linux VMs) without Judge0's cgroup-v1
  requirement.

## Prerequisites

- **Node.js 18+**
- **Docker Desktop** (to run the local Piston sandbox). On Windows use the WSL2 backend.

## Getting started

### 1. Start the Piston sandbox (once)

```bash
cd algoforge-ide
docker compose up -d piston
# Piston API comes up at http://localhost:2000
```

### 2. Install language runtimes (once)

Piston ships with **no languages installed** — install the ones you need via its package
API. To install everything this app supports:

```powershell
$installs = @(
  @{l='python';    v='3.12.0'},
  @{l='java';      v='15.0.2'},
  @{l='node';      v='20.11.1'},   # javascript
  @{l='typescript';v='5.0.3'},
  @{l='gcc';       v='10.2.0'},    # c and c++
  @{l='go';        v='1.16.2'}
)
foreach ($i in $installs) {
  Invoke-RestMethod -Method Post -Uri "http://localhost:2000/api/v2/packages" `
    -ContentType "application/json" -Body (@{language=$i.l; version=$i.v} | ConvertTo-Json)
}
```

(See available versions at `GET http://localhost:2000/api/v2/packages`.)

### 3. Configure and run the app

```bash
cp .env.example .env          # defaults point PISTON_URL at http://localhost:2000
npm install
npm run dev
```

Then open http://localhost:5173.

- `npm run dev` — starts the Express API (:3001) and the Vite dev server (:5173).
- `npm run server` — API only. `npm run web` — frontend only.
- `npm run build` — type-checks and builds the production bundle.

## Running the backend fully containerized

The API is containerized ([Dockerfile](./Dockerfile)) and can run alongside Piston via
Compose. This builds the frontend into the image and serves it from the API, so a single
container hosts the whole app:

```bash
docker compose up -d          # API on :3001 (also serves the built UI), Piston on :2000
```

In disk mode the API reads the repo mounted read-only at `/repo` (the parent folder of
`algoforge-ide/`). Health probe: `GET /healthz`.

## File source: disk vs Azure Blob

The file tree/content is served through a small store abstraction
([server/fileStore.mjs](./server/fileStore.mjs)), selected by `FILE_SOURCE`:

- **`disk`** (default) — walks the local repo checkout. Best for development.
- **`blob`** — reads from an Azure Blob Storage container. Used in the cloud.

To populate Blob storage from the repo (run once, and again when files change):

```bash
# passwordless (az login / managed identity):
AZURE_STORAGE_ACCOUNT_URL=https://<account>.blob.core.windows.net npm run upload:blob
# or with a connection string:
AZURE_STORAGE_CONNECTION_STRING="..." npm run upload:blob
```

Then run the API with `FILE_SOURCE=blob` and the same `AZURE_STORAGE_*` settings. In Azure,
prefer `AZURE_STORAGE_ACCOUNT_URL` + a managed identity (no secrets in config).

> Without Piston running, the app still loads and browses files; **Run Code** returns a
> clear "Sandbox unreachable" message instead of executing. If a language's runtime
> isn't installed, it returns "Runtime not installed".

## How to practice

1. Pick a file in the left explorer. Its content loads in the **Standard Solution** pane.
2. Write a **complete, runnable program** in the **Your Workspace** pane (the starter
   template for each language is a valid program). The sandbox runs whole programs that
   read **stdin** and write **stdout**, like an online judge.
3. Enter any **Stdin** in the bottom panel.
4. Click **Run Code** (or press `Ctrl/Cmd+R`).
   - **Console** tab: stdout, plus compile/runtime errors.
   - **Output** tab: your program's stdout.
   - **Tests** tab: PASS/FAIL comparing your stdout to the standard solution.
     Comparison is **skipped** when the standard file isn't a complete program
     (e.g. a bare `Solution` class with no `main`), which is common in this repo.

## Configuration

- `COMPANY_FOLDERS` (env, comma-separated) controls which top-level folders are
  "Companies" (defaults to `Amazon`). `IGNORED` in `server/fileStore.mjs` lists folders
  hidden from the explorer.
- `.env` controls the port, `FILE_SOURCE`, `PISTON_URL`, per-run limits, and optional
  per-language Piston version pins. See `.env.example`.
- The Piston container's limit ceilings (run/compile timeout, output size) are set via
  `PISTON_*` env vars in `docker-compose.yml`.

## Deploying to Azure

Infrastructure is defined as code (Bicep) under [infra/](./infra). Topology:

| Piece | Azure service | Bicep module |
| --- | --- | --- |
| Frontend (React build) | Azure Static Web Apps | `modules/staticwebapp.bicep` |
| Express API | Azure Container Apps (VNet-integrated) | `modules/containerapp.bicep` |
| Piston sandbox | Piston on a private Linux VM | `modules/piston-vm.bicep` |
| Networking | VNet + subnets + NSG | `modules/network.bicep` |
| Algorithm files | Azure Blob Storage | `modules/storage.bicep` |
| Image registry | Azure Container Registry | `modules/acr.bicep` |
| Secrets | Azure Key Vault | `modules/keyvault.bicep` |
| Monitoring | App Insights + Log Analytics | `modules/monitoring.bicep` |
| Least-privilege RBAC | Managed-identity role assignments | `modules/rbac.bicep` |

**Security essentials:** the Piston VM has **no public IP** and sits on a private subnet
reachable **only** from the apps subnet on port 2000 (NSG). The API authenticates to Blob,
ACR, and Key Vault with a **system-assigned managed identity** (no secrets in config) via
least-privilege roles. Piston runs on cgroup v2, so a current Ubuntu 22.04 image works
without kernel changes.

### Deploy — fully automated (recommended)

One command provisions all Azure infra, creates the GitHub OIDC identity + roles, sets the
repo secrets/variables, and (with `-Push`) triggers the CI/CD deploy. You only need to be
logged in first:

```powershell
az login          # interactive — the one manual security gate
gh auth login     # interactive — GitHub CLI auth

cd algoforge-ide
./infra/bootstrap.ps1 -Push
```

That's it. `bootstrap.ps1`:
1. Runs preflight checks (az / gh / node / git present and authenticated).
2. Ensures an SSH key for the Piston VM (generates one if missing).
3. Creates the resource group and deploys the Bicep infrastructure.
4. Creates the Entra app + federated credentials and grants least-privilege roles.
5. Reads the deployment outputs and sets all GitHub Secrets & Variables via `gh`.
6. With `-Push`, commits and pushes to `master`, triggering the Deploy workflow (which
   builds the image, seeds Blob, and ships the frontend). Omit `-Push` to review first.

Watch the deployment: `gh run watch -R Ivesonchen/Summary`.

> Only `az login` and `gh auth login` are interactive — they can't be scripted away (by
> design). Everything else is automatic, and the script is safe to re-run (idempotent).

### Deploy — manual / step-by-step

```powershell
# Provision + push image + files + frontend directly from your machine:
./infra/deploy.ps1 -ResourceGroup algoforge-rg -Location eastus2 `
  -SshPublicKeyPath $HOME\.ssh\id_ed25519.pub
```

Or run the underlying `az` commands yourself (two-phase, so the API Container App is only
created after its image exists — a placeholder on the wrong port would fail health checks):

```bash
az group create -n algoforge-rg -l eastus2

# Phase 1: base infra without the API Container App.
az deployment group create -g algoforge-rg -f infra/main.bicep \
  -p infra/main.parameters.json -p pistonSshPublicKey="$(cat ~/.ssh/id_ed25519.pub)" \
  -p deployApi=false

# Build & push the real API image (listens on 3001).
az acr build -r <acrName> -t algoforge-api:latest -f Dockerfile .

# Phase 2: create the Container App pointing at the real image.
az deployment group create -g algoforge-rg -f infra/main.bicep \
  -p infra/main.parameters.json -p pistonSshPublicKey="$(cat ~/.ssh/id_ed25519.pub)" \
  -p deployApi=true apiImage=<acr>.azurecr.io/algoforge-api:latest

AZURE_STORAGE_ACCOUNT_URL=https://<storage>.blob.core.windows.net npm run upload:blob
```

> **Why two phases:** the API Container App's ingress targets port 3001. If it were created
> before the real image exists, a placeholder image on a different port never passes the
> health check and provisioning fails with `Operation expired`. Phase 1 builds the base
> infra + ACR; phase 2 creates the app once the port-3001 image is pushed. `bootstrap.ps1`
> does both automatically. The frontend is built with `VITE_API_BASE=<api-url>` so it calls
> the Container Apps API origin (relative `/api` is used only in local dev via the Vite proxy).

Validate templates locally with `bicep build infra/main.bicep`.

## CI/CD (GitHub Actions)

Two workflows live at the repo root under `.github/workflows/` (path-filtered to
`algoforge-ide/**`):

- **`ci.yml`** — on every push/PR: `npm run build` (tsc + Vite), a Docker image build,
  and `bicep build` validation.
- **`deploy.yml`** — on push to `master` (or manual dispatch): builds & pushes the API
  image to ACR (`az acr build`), rolls it out to the Container App, syncs algorithm files
  to Blob, and builds & deploys the frontend to Static Web Apps.

Auth is **passwordless via OIDC** (federated credentials) — no cloud secrets stored in
GitHub beyond IDs. `bootstrap.ps1` sets everything up automatically; to (re)configure just
the OIDC identity and print the values manually, use:

```powershell
./infra/setup-github-oidc.ps1 -ResourceGroup algoforge-rg -Repo Ivesonchen/Summary
```

That script creates the Entra app + federated credentials and prints the values to add
under **GitHub → Settings → Secrets and variables → Actions**:

| Secrets | Variables |
| --- | --- |
| `AZURE_CLIENT_ID` | `AZURE_RESOURCE_GROUP` |
| `AZURE_TENANT_ID` | `ACR_NAME`, `ACR_LOGIN_SERVER` |
| `AZURE_SUBSCRIPTION_ID` | `CONTAINER_APP_NAME`, `STATIC_WEB_APP_NAME` |
| `AZURE_STATIC_WEB_APPS_API_TOKEN` | `AZURE_STORAGE_ACCOUNT_URL`, `API_URL` |

The variable values come from the Bicep deployment outputs (`az deployment group show`).
