# $1 -- filename with task

# will be binded to container's 4444
initialSeleniumPort=7100;
# will be binded to container's 5762 or something
initialRMQPort=9100;
# will be conctated with order number _X
initialSeleniumNodeName="node"
# will be conctated with order number _X
initialRMQServerName="appRabbit"

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
while read -r line; do
	if [ "$i" -eq "0" ]; then
		mode[j]="$line"
		echo "-->found mode $mode"
		
		# if we've found nestor, we won't find any other credentials, so continue
		if [ "$mode[j]" == "nestor" ]
			then
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
echo "-->taskfile parsed with result of $j entity(ies)"

# Starting Listener
echo "-->starging node"
if [ "$mode" == "listener" ]
then
	./stop-node.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode" "$email" "$password" "$targetLink" "$targetId"
	./stop-node.sh
	exit 0
fi

# Starting Poster
if [ "$mode" == "poster" ]
then
	./stop-node.sh
	./start-node.sh
	
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode" "$email" "$password" "$targetLink" "$targetId"
	./stop-node.sh
	exit 0
fi  

# Starting Nestor
if [ "$mode" == "nestor" ]
then
	java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode"
	exit 0
fi

# No valid mode
echo "-->No valid mode, exiting"
exit 0