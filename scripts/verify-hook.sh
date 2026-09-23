#!/usr/bin/env bash
# The hook, measured. One artifact per framework, built ONCE with
# payments.fraud-check.enabled=false. Then the same artifact is started with
# the property set to true at RUNTIME, and the endpoint reports whether the
# FraudCheck bean exists.
#
#   Spring   @ConditionalOnProperty is evaluated at startup   -> present
#   Quarkus  @IfBuildProperty was decided during the build    -> absent
#
# Each case asserts its expectation and fails rather than printing something
# plausible.
set -euo pipefail
cd "$(dirname "$0")/.."
export JAVA_HOME="${JAVA_HOME_25:-$HOME/.sdkman/candidates/java/25.0.4-amzn}"
JAVA="$JAVA_HOME/bin/java"

(cd spring-payments && ./mvnw -q -DskipTests package)
(cd quarkus-payments && ./mvnw -q -DskipTests package)

ask() { # port
  for _ in $(seq 1 60); do
    if out=$(curl -sf "http://localhost:$1/fraud-check"); then echo "$out"; return 0; fi
    sleep 0.5
  done
  echo "no answer on $1" >&2; return 1
}
expect() { # label actual wanted
  if [[ "$2" != *"\"fraudCheck\":\"$3\""* ]]; then
    echo "CLAIM FAILED: $1 expected $3, got $2" >&2; exit 1
  fi
}

echo "=== Built with payments.fraud-check.enabled=false. Started with it set to true. ==="

"$JAVA" -jar spring-payments/target/spring-payments-0.0.1-SNAPSHOT.jar \
  --server.port=8081 --payments.fraud-check.enabled=true >/tmp/spring-hook.log 2>&1 &
SP=$!
"$JAVA" -Dquarkus.http.port=8082 -Dpayments.fraud-check.enabled=true \
  -jar quarkus-payments/target/quarkus-app/quarkus-run.jar >/tmp/quarkus-hook.log 2>&1 &
QP=$!
trap 'kill $SP $QP 2>/dev/null || true' EXIT

S=$(ask 8081); Q=$(ask 8082)
echo "Spring   GET /fraud-check  $S"
echo "Quarkus  GET /fraud-check  $Q"
expect Spring "$S" present
expect Quarkus "$Q" absent

echo
echo "=== What Quarkus said about the runtime value ==="
grep -i "build time\|build-time\|fraud-check" /tmp/quarkus-hook.log || echo "(nothing)"
echo
echo "hook verified: same property, same override, Spring built the bean and Quarkus did not"
