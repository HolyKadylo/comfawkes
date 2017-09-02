# This starts openjdk container
# $1 name
# $2 args

echo "-->starting java app with params $2 $3 $4 $5 $6 $7 $8 $9 ${10}"
echo "-->non-java params $1 ${11}"
if [ "${11}" != "" ]; then

	# Node, needs linking to Selenium
	sudo docker run -d -h "$1" --name "$1" --link "${11}" -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar $2 $3 $4 $5 $6 $7 $8 $9 ${10}
else
	
	# Nestor, doesn't need linking to Selenium
	sudo docker run -d -h "$1" --name "$1" -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk java -jar target/comfawkes-1.0-SNAPSHOT-jar-with-dependencies.jar $2 $3 $4 $5 $6 $7 $8 $9 ${10}
echo "-->java app starting done"