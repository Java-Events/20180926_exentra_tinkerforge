# 20180926_exentra_tinkerforge
A TinkerForge Java Event


## Docker 

+ docker run -d --restart=always --name=socat -p 2376:2375 -v /var/run/docker.sock:/var/run/docker.sock alpine/socat TCP4-LISTEN:2375,fork,reuseaddr UNIX-CONNECT:/var/run/docker.sock
