# $1 name of the container
echo "-->stopping container $1"
sudo docker stop "$1"
sudo docker rm "$1"
echo "-->done stopping container $1"