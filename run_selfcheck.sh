#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
java -cp out ru.edu.pr03.SelfCheck "$@"
