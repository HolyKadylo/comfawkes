# $1 -- filename with task
initialNodePort=5000;
initialRMQPort=5672;

echo "-->Setting initial node port to $initialNodePort"
echo "-->Setting initial RMQ port to $initialRMQPort"
echo "-->Running in $1 mode"

# reading task file
i=0
echo "-->parsing taskfile $2"
while read -r line; do
	if [i == 0]; then
		mode = "$line"
		echo "-->found mode $mode"

	if [i == 1]; then
		email = "$line"
		echo "-->found email $email"
	fi
	
	if [i == 2]; then
		password = "$line"
		echo "-->found password $password"
	fi
	
	if [i == 3]; then
		targetLink = "$line"
		echo "-->found targetLink $targetLink"
	fi
	
	if [i == 4]; then
		targetId = "$line"
		echo "-->found targetId $targetId"
	fi
	i=i + 1
done < $1
echo "-->taskfile parsed"

# Starting Listener
echo "-->starging node"
if [ "$1" == "listener" ]
then
	./stop-node.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$1" "$email" "$password" "$targetLink" "$targetId"
	./stop-node.sh
	exit 0
fi

# Starting Poster
if [ "$1" == "poster" ]
then
	./stop-node.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$1" "$email" "$password" "$targetLink" "$targetId"
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