#!/bin/bash

if [ "$1" != "from-gradle" -a "$1" != "-i" ]; then
  echo "Please don't run this script directly. Instead do a ./gradlew integrationTests or a ./gradlew check from the project root."
  echo "The case when you run may this script manually is when running it as $0 -i, which will invoke cram with -i."
  exit 1
fi

[ "$1" == "from-gradle" ] && shift

cleanup() {
  echo "Stopping DynamoDB local and API"
  kill %1
  kill %2
  echo "DynamoDB and API logs are in $testdir/dynamodb.log and $testdir/api.log"
}
trap cleanup EXIT

testdir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
rootdir=$(dirname $testdir)

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
java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -inMemory > $testdir/dynamodb.log 2>&1 &

JAVA_OPTS="$JAVA_OPTS -Dfail.test.fixedUnitTimestamp=0"
JAVA_OPTS="$JAVA_OPTS -Dfail.db.dynamoDBEndpoint=http://localhost:8000"
JAVA_OPTS="$JAVA_OPTS -Daws.accessKeyId=foo -Daws.secretKey=bar"
export JAVA_OPTS

echo "JAVA_OPTS=${JAVA_OPTS}"

echo "Starting API server"
$rootdir/api/server/build/install/fail-api/bin/fail-api > $testdir/api.log 2>&1 &

echo "Sleeping 2 seconds to make sure both the API and DynamoDB local come up"
sleep 2

echo "Running tests"
PATH="$testdir:$PATH" cram $testdir/cases $@
exit $?

