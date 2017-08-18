# $1 name of the node
# $2 port

# TODO find out if -h may be the same for all nodes
sudo docker run -d -p "$2":4444 -P --name "$1" -h "$1" selenium/standalone-firefox