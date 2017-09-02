# This script starts Docker nodes on the particular
# real machine. It is guided with the task file

# $1 -- filename with task

# initial port 
# will be binded to container's 4444
initialSeleniumPort=7101;

# initial port 
# will be binded to container's 5762 or something
RMQPort=9101;

# will be conctated with order number _X
seleniumNodeName="node_"

# will be conctated with order number _X
applicationNodeName="app_"

# we need only one RMQ per JVM TODO find out
RMQServerName="appRabbit"
seleniumPort=$initialSeleniumPort # <- there is value
timeout=5s
echo "-->Setting permissions"
sudo chmod +x *.sh
echo "-->Setting initial node port to $seleniumPort"
echo "-->Setting initial RMQ port to $RMQPort"

# reading RMQ cookie file
echo "-->reading RMQ cookie file"
RMQcookie="`cat RMQ_COOKIE`"
echo "-->have read: $RMQcookie"

# reading task file
# i -- iterator through types of variables
# j -- iterator through desired nodes
i=0
j=0
echo "-->parsing taskfile $1"
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

# Starting RMQ server (single per this JVM)
echo "-->starting RMQ"
./stop-container.sh "$RMQServerName"
./start-rmq.sh "$RMQServerName" "$RMQPort" "$RMQcookie"
echo "-->RMQ $RMQServerName started at port $RMQPort"

	# node
	# args[0] -- role of the application
	# args[1] -- email
	# args[2] -- password
	# args[3] -- publicAddress
	# args[4] -- publicId
	# args[5] -- host & port on host for selenium
	# args[6] -- RMQ cookie
	# args[7] -- port on host for RMQ
	# args[8] -- host for RMQ
	
	# nestor
	# args[0] -- role of the application
	# args[1] -- RMQ cookie
	# args[2] -- port on host for RMQ
	# args[3] -- host for RMQ


echo "-->forming parameter strings and launching..."
nodesCount=0
for ((i=0; i<=$j; i++)); do
	args=""
	
	# part may vary from nestor to node
	args+="${mode[i]}"

	if [ "${email[i]}" != "" ]; then
		args+=" ${email[i]}"
	fi
	if [ "${password[i]}" != "" ]; then
		args+=" ${password[i]}"
	fi
	if [ "${targetLink[i]}" != "" ]; then
		args+=" ${targetLink[i]}"
	fi
	if [ "${targetId[i]}" != "" ]; then
		args+=" ${targetId[i]}"
	fi
	
	# preparing to link to selenium nodes
	if [ "${mode[j]}" != "nestor" ]; then
		# args+=" http://""$seleniumNodeName""$nodesCount"":""$seleniumPort"
		args+=" $seleniumNodeName""$nodesCount"":""$seleniumPort"
		# echo "-->args[5] is: http://""$seleniumNodeName""$nodesCount"":""$seleniumPort"
	fi
	
	# part is constant for nestor and node
	args+=" $RMQcookie $RMQPort $RMQServerName"
	
	# launching selenium node
	./stop-container.sh "$seleniumNodeName""$nodesCount"
	./start-node.sh "$seleniumNodeName$nodesCount" "$seleniumPort"
	
	sleep "$timeout"
	
	# launching application
	./stop-container.sh "$applicationNodeName""$nodesCount"
	
	if [ "${mode[j]}" != "nestor" ]; then
		./start-openjdk.sh "$applicationNodeName$nodesCount" $args "$seleniumNodeName$nodesCount"
	else
		./start-openjdk.sh $args # <-- there is value
	fi
	
	((nodesCount++))
	((seleniumPort++))
done
echo "-->done run.sh"



# Starting App
# echo "-->starging app"

# # Stopping as much selenium containers as we need to start
# for ((i=1; i<=$j; i++)); do
	# ./stop-container.sh "$seleniumNodeName""_$i"
# done

# # Starting selenium containers
# for ((i=1; i<=$j; i++)); do
	# ./start-node.sh "$seleniumNodeName""_$i" "$seleniumPort"
	# ((seleniumPort++))
# done

# # actually starting
# java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar $args #<--is

# Stopping RMQ server
# ./stop-container.sh "$RMQServerName"
# # Stopping launched selenium containers
# for ((i=1; i<=$j; i++)); do
	# ./stop-container.sh "$seleniumNodeName""_$i"
# done

exit 0