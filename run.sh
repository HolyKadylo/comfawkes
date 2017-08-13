# $1 -- mode
# $2 -- filename with account
# $3 -- filename with target public

echo "-->Running in $1 mode"

# reading account file
i = 0
while read -r line; do
	if [i == 0]; then
		email = "$line"
	fi
	
	if [i == 1]; then
		password = "$line"
	fi
	i = i + 1
done < accnt

# reading target file
i = 0
while read -r line; do
	if [i == 0]; then
		targetLink = "$line"
	fi
	
	if [i == 0]; then
		targetId = "$line"
	fi
	i = i + 1
done < trgt

# Starting Listener
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