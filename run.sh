echo "-->Running in $1 mode"
if ["$1" == "node"]
then
	mode="-node"
else
	if ["$1" == "nestor"]
	then 
		mode="-nestor"
	else
		echo "-->Mode disabled, exiting"
		exit 0
	fi
fi

# Starting Node
if ["$mode" == "-node"]
then
	echo "-->Starting App.java with $mode"
	./stop-node.sh
	./start-node.sh
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar $mode
	./stop-node.sh
fi 

# Starting Nestor
if ["$mode" == "-nestor"]
then
	echo "-->Starting App.java with $mode"
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar $mode
fi
