# This starts openjdk container
echo "-->starting java app"
sudo docker run -d -v "$PWD":/usr/src/myapp -w /usr/src/myapp openjdk java App "$1"
echo "-->java app done"