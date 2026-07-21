# Multi-stage build for the AlgoForge API + built frontend in one image.
#
# The image runs the Express API (server/) and also serves the compiled React UI
# from /app/dist, so a single container can host the whole app. In Azure you can
# either use this image behind Container Apps, or serve the UI from Static Web Apps
# and use this image for /api only.

# --- Stage 1: build the frontend ---
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
# CACHEBUST (set per-commit in CI) forces the source COPY layer to rebuild so a
# stale ACR Tasks layer cache can never serve old code.
ARG CACHEBUST=dev
RUN echo "cachebust=$CACHEBUST"
COPY . .
RUN npm run build

# --- Stage 2: runtime ---
# Debian-slim (glibc), NOT alpine: the optional GitHub Copilot CLI binary
# (@github/copilot-linux-x64, used by the AI_PROVIDER=copilot path) is a native
# glibc executable and will not run against Alpine's musl libc.
FROM node:20-slim AS runtime
WORKDIR /app
ENV NODE_ENV=production
ENV PORT=3001
# Home dir for the `node` user; the Copilot CLI persists its session under
# $HOME/.copilot (mount a volume there in production to survive restarts).
ENV HOME=/home/node

# Install production dependencies only. On this linux/amd64 image npm also pulls
# the optional @github/copilot-linux-x64 binary needed by the copilot provider.
COPY package*.json ./
RUN npm ci --omit=dev

# App code + built UI.
COPY server ./server
COPY --from=build /app/dist ./dist

# Ensure the Copilot config dir exists and is owned by the runtime user so the
# CLI can write its session there (and so a mounted volume inherits ownership).
RUN mkdir -p /home/node/.copilot && chown -R node:node /home/node

EXPOSE 3001
USER node

# Basic container healthcheck against the API health probe.
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD node -e "fetch('http://localhost:'+(process.env.PORT||3001)+'/healthz').then(r=>process.exit(r.ok?0:1)).catch(()=>process.exit(1))"

CMD ["node", "server/index.mjs"]
