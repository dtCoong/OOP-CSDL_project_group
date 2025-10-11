#!/usr/bin/env bash
set -e
BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"
SRC_DIR="$BASE_DIR/src/main/java"
LIB_DIR="$BASE_DIR/libs"
OUT_DIR="$BASE_DIR/out"
mkdir -p "$OUT_DIR"
CP=".:$LIB_DIR/*"
echo "Compiling..."
javac -encoding UTF-8 -cp "$CP" -d "$OUT_DIR" $(find "$SRC_DIR" -name "*.java")
echo "Done. Classes in $OUT_DIR"
