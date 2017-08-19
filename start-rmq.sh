# $1 -- name
# $2 -- port
# $3 -- cookie

# https://www.rabbitmq.com/tutorials/tutorial-one-java.html
defaultPort=5672

echo "-->starting RMQ server $1 at port $2 with cookie $3 and defaultPort $defaultPort"
sudo docker run -d --hostname "$1" --name "$1" -e RABBITMQ_ERLANG_COOKIE="$3" -p "$2":"$defaultPort" rabbitmq
echo "-->done"