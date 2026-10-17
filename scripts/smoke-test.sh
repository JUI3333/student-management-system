#!/usr/bin/env bash
# Starts the packaged JAR and checks that the real API answers correctly.
# Usage: bash scripts/smoke-test.sh target/student-management-system.jar
set -euo pipefail

JAR="${1:?Usage: smoke-test.sh <path-to-jar>}"
URL="http://localhost:8080/api/students"

java -jar "$JAR" > app.log 2>&1 &
APP_PID=$!
trap 'kill "$APP_PID" 2>/dev/null || true' EXIT

echo "Waiting for the application to start..."
STARTED=false
for _ in $(seq 1 30); do
  if curl -sf "$URL" > /dev/null; then
    STARTED=true
    break
  fi
  sleep 2
done
if [ "$STARTED" != "true" ]; then
  echo "Application did not start in time. Log:"
  cat app.log
  exit 1
fi

BODY='{"firstName":"Smoke","lastName":"Test","email":"smoke@example.com","course":"IT"}'

CODE=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H 'Content-Type: application/json' -d "$BODY" "$URL")
[ "$CODE" = "201" ] || { echo "POST expected 201 but got $CODE"; exit 1; }
echo "POST /api/students -> 201 OK"

CODE=$(curl -s -o /dev/null -w '%{http_code}' -X POST -H 'Content-Type: application/json' -d "$BODY" "$URL")
[ "$CODE" = "409" ] || { echo "Duplicate POST expected 409 but got $CODE"; exit 1; }
echo "Duplicate POST -> 409 OK"

curl -sf "$URL" | grep -q "smoke@example.com" || { echo "Created student not found in list"; exit 1; }
echo "GET /api/students lists the new student OK"

echo "SMOKE TEST PASSED"
