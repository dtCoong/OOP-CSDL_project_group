#!/usr/bin/env bash
set -e
BASE_DIR="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="$BASE_DIR/out"
LIB_DIR="$BASE_DIR/libs"
CP="$OUT_DIR:$LIB_DIR/*"
echo "Running MainDemo ..."
java -cp "$CP" com.mycompany.ehr.model.MainDemo
