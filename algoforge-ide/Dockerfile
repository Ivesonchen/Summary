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
COPY . .
RUN npm run build

# --- Stage 2: runtime ---
FROM node:20-alpine AS runtime
WORKDIR /app
ENV NODE_ENV=production
ENV PORT=3001

# Install production dependencies only.
COPY package*.json ./
RUN npm ci --omit=dev

# App code + built UI.
COPY server ./server
COPY --from=build /app/dist ./dist

EXPOSE 3001
USER node

# Basic container healthcheck against the API health probe.
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD node -e "fetch('http://localhost:'+(process.env.PORT||3001)+'/healthz').then(r=>process.exit(r.ok?0:1)).catch(()=>process.exit(1))"

CMD ["node", "server/index.mjs"]
