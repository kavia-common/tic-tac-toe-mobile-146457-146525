#!/bin/bash
cd /home/kavia/workspace/code-generation/tic-tac-toe-mobile-146457-146525/mobile_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

