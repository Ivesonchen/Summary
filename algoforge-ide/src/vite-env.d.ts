/// <reference types="vite/client" />

interface ImportMetaEnv {
  /** Base origin for the API (e.g. https://algoforge-api.<region>.azurecontainerapps.io). Empty in dev. */
  readonly VITE_API_BASE?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
