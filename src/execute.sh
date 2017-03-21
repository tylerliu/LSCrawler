javac LSCrawler.java
while true
do
    read -p "Do you wish to crawl the photos? " ans
    case $ans in
        [Yy]      ) java LSCrawler;;
        [Qq]uit   ) break;;
	* ) echo "It seems that you refused to fetch the Photos..."
    esac
    echo
done
rm LSCrawler.class
