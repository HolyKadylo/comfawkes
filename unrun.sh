# This script stops as many Docker containers
# as specified in $1

# will be conctated with order number _X
seleniumNodeName="node_"

# will be conctated with order number _X
applicationNodeName="app_"

# we need only one RMQ per JVM TODO find out
RMQServerName="appRabbit"

echo "-->Stopping RMQServer"
./stop-container.sh "$RMQServerName"

echo "-->Stopping apps and nodes"
nodesCount=0
i=0
for ((i=0; i<="$1"; i++)); do
	
	# stopping selenium node
	./stop-container.sh "$seleniumNodeName""$nodesCount"
	
	# stopping application
	./stop-container.sh "$applicationNodeName""$nodesCount"
	((nodesCount++))
done
echo "-->done unrun.sh"

exit 0