#!/bin/bash

testdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
rootdir=$(dirname $testdir)
$rootdir/gradlew api:server:installApp cli:installApp

if [ ! -e $testdir/venv/bin/activate ]; then
  echo 'Virtualenv with cram not found, creating'
  virtualenv $testdir/venv
  . $testdir/venv/bin/activate
  pip install cram
else
  echo 'Activating virtualenv'
  . $testdir/venv/bin/activate
fi

if [ ! -e $testdir/fail ]; then
  echo 'Symlink to fail cli not found, creating'
  ln -s $rootdir/cli/build/install/fail/bin/fail $testdir/
fi

mkdir -p dynamodb-local
cd dynamodb-local
if [ ! -e DynamoDBLocal.jar ]; then
  echo "DynamoDB local not found, downloading"
  wget http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz
  tar -xzf dynamodb_local_latest.tar.gz
fi
echo "Starting DynamoDB local"
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -inMemory &

echo "Starting API server"
JAVA_OPTS="$JAVA_OPTS -Dfail.test.fixedUnitTimestamp=0 -Dfail.db.dynamoDBEndpoint=http://localhost:8000" $rootdir/api/server/build/install/fail-api/bin/fail-api &

echo "Sleeping 2 seconds to make sure both the API and DynamoDB local come up"
sleep 2

echo "Running tests"
PATH="$testdir:$PATH" cram $testdir/cases $@

kill %1
kill %2
