#!/bin/bash
set -e

SDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

cd $SDIR/..
sbt universal:packageBin
scp target/universal/queuegarden-0.1.0-SNAPSHOT.zip pi@raspberrypi.local:~/
