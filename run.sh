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

echo "-->Setting initial node port to $seleniumPort"
echo "-->Setting initial RMQ port to $RMQPort"
echo "-->Running in $1 mode"

# reading RMQ cookie file
echo "-->reading RMQ cookie file"
RMQcookie="`cat RMQ_COOKIE`"
echo "-->have read: $RMQcookie"

# reading task file
# i -- iterator through types of variables
# j -- iterator through desired nodes
i=0
j=0
echo "-->parsing taskfile $2"
while read -r line || [[ -n "$line" ]]; do
	if [ "$i" -eq "0" ]; then
		mode[j]="$line"
		echo "-->found mode ${mode[j]}"
		
		# if we've found nestor, we won't find any other credentials, so continue
		if [ "${mode[j]}" == "nestor" ]; then
			i=-1
			((j++))
		fi		
	fi

	if [ "$i" -eq "1" ]; then
		email[j]="$line"
		echo "-->found email ${email[j]}"
	fi
	
	if [ "$i" -eq "2" ]; then
		password[j]="$line"
		echo "-->found password ${password[j]}"
	fi
	
	if [ "$i" -eq "3" ]; then
		targetLink[j]="$line"
		echo "-->found targetLink ${targetLink[j]}"
	fi
	
	if [ "$i" -eq "4" ]; then
		targetId[j]="$line"
		echo "-->found targetId ${targetId[j]}"
		i=-1
		((j++))
	fi
	((i++))
done < "$1"
echo "-->taskfile parsed with result of $j entity(ies)"

echo "-->forming parameter strings for nodes"
args=""
for ((i=0; i<=$j; i++)); do
	if [ "$i" -eq "0" ]; then
		args+="${mode[i]}"
	else
		args+=" ${mode[i]}"
	fi
	if [ "${email[i]}" != "" ]; then
		args+=" ${email[i]}"
	fi
	if [ "${password[j]}" != "" ]; then
		args+=" ${password[j]}"
	fi
	if [ "${targetLink[j]}" != "" ]; then
		args+=" ${targetLink[j]}"
	fi
	if [ "${targetId[j]}" != "" ]; then
		args+=" ${targetId[j]}"
	fi
done
echo "-->parameter strings for nodes formed: $args"

# Starting RMQ server (single per this JVM)
echo "-->starting RMQ"
./stop-container.sh "$RMQServerName"
./start-rmq.sh "$RMQServerName" "$RMQPort" "$RMQcookie"
echo "-->RMQ $RMQServerName started at port $RMQPort"

# Starting App
echo "-->starging app"

# Stopping as much selenium containers as we need to start
for ((i=1; i<=$j; i++)); do
	./stop-container.sh "$seleniumNodeName""_$i"
done

# Starting selenium containers
for ((i=1; i<=$j; i++)); do
	./start-node.sh "$seleniumNodeName""_$i" "$seleniumPort"
	((seleniumPort++))
done

# args[0] -- role of the application
# args[1] -- email
# args[2] -- password
# args[3] -- publicAddress
# args[4] -- publicId

java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar "$mode" "$email" "$password" "$targetLink" "$targetId"

# Stopping RMQ server
./stop-container.sh "$RMQServerName"
# Stopping launched selenium containers
for ((i=1; i<=$j; i++)); do
	./stop-container.sh "$seleniumNodeName""_$i"
done

exit 0