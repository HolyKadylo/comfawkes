# Runs to setup enviroment
# Ubuntu linux64
sudo apt-get update
sudo apt-get install docker-io
sudo apt-get install git
sudo apt-get install maven
sudo docker pull selenium/standalone-firefox
wget https://github.com/mozilla/geckodriver/releases/download/v0.17.0/geckodriver-v0.17.0-linux64.tar.gz
tar -xvf geckodriver-v0.17.0-linux64.tar.gz
rm geckodriver-v0.17.0-linux64.tar.gz
git init
git remote add fawkes https://github.com/holykadylo/comfawkes
git pull fawkes master
mvn package