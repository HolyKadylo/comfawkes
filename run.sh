# $1 -- filename with task

# initial port 
# will be binded to container's 4444
seleniumPort=7101;
# initial port 
# will be binded to container's 5762 or something
RMQPort=9101;
# will be conctated with order number _X
seleniumNodeName="node"
# we need only one RMQ per JVM TODO find out
RMQServerName="appRabbit"

echo "-->Setting initial node port to $initialNodePort"
echo "-->Setting initial RMQ port to $initialRMQPort"
echo "-->Running in $1 mode"

# reading RMQ cookie file
echo "-->reading RMQ cookie file"
RMQcookie="`cat RMQ_COOKIE`"
echo "-->have read $RMQcookie"

# reading task file
# i -- iterator through types of variables
# j -- iterator through desired nodes
i=0
j=0
echo "-->parsing taskfile $2"
while read -r line || [[ -n "$line" ]]; do
	if [ "$i" -eq "0" ]; then
		mode[j]="$line"
		echo "-->found mode $mode"
		
		# if we've found nestor, we won't find any other credentials, so continue
		if [ "$mode[j]" == "nestor" ]; then
			i=0
			((j++))
		fi		
	fi

	if [ "$i" -eq "1" ]; then
		email[j]="$line"
		echo "-->found email $email"
	fi
	
	if [ "$i" -eq "2" ]; then
		password[j]="$line"
		echo "-->found password $password"
	fi
	
	if [ "$i" -eq "3" ]; then
		targetLink[j]="$line"
		echo "-->found targetLink $targetLink"
	fi
	
	if [ "$i" -eq "4" ]; then
		targetId[j]="$line"
		echo "-->found targetId $targetId"
		i=-1
		((j++))
	fi
	((i++))
done < "$1"

((j++))
echo "-->taskfile parsed with result of $j entity(ies)"
((j--))

# Starting RMQ server
echo "-->starting RMQ"
./stop-container.sh "$RMQServerName"
./start-rmq.sh "$RMQServerName" "$RMQPort" "$RMQcookie"
echo "-->RMQ $RMQServerName started at port $RMQPort"

# Starting Listener
echo "-->starging node"
if [ "$mode" == "listener" ]
then
	./stop-container.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode" "$email" "$password" "$targetLink" "$targetId"
	./stop-container.sh
	exit 0
fi

# Starting Poster
if [ "$mode" == "poster" ]
then
	./stop-container.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode" "$email" "$password" "$targetLink" "$targetId"
	./stop-container.sh
	exit 0
fi  

# Starting Nestor
if [ "$mode" == "nestor" ]
then
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode"
	exit 0
fi

# No valid mode
echo "-->Invalid file, exiting"
exit 0