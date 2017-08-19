# $1 name of the node
# $2 port

# TODO find out if -h may be the same for all nodes
echo "-->Starting selenium node $1 at port $2"
sudo docker run -d -p "$2":4444 -P --name "$1" -h "$1" selenium/standalone-firefox
echo "-->done"