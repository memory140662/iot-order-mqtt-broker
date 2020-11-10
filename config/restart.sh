if [ ! -z $(sudo lsof -t -i :8080) ]
then
        sudo kill $(sudo lsof -t -i :8080)
fi

nohup java -jar $JAR &