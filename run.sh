echo "-->Running in $1 mode"

# Starting Node
if [ "$1" == "node" ]
then
	./stop-node.sh
	./start-node.sh
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$1"
	./stop-node.sh
	exit 0
fi 

# Starting Nestor
if [ "$1" == "nestor" ]
then
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$1"
	exit 0
fi

# No valid mode
echo "-->No valid mode, exiting"
exit 0