set -x
#python mergeOutput.py
#wait
#mv sensor.txt sensor$1_markov-s1.3-dis5.txt
#python mergeOutputRand.py
#wait
#mv sensor.txt sensor$1_rand-s1.3-dis5.txt
#python mergeOutputRand-s1.5.py
#wait
#mv sensor.txt sensor$1_rand-s1.5-dis5.txt
python mergeOutputRand-s1.1-dis5.py
wait
cp sensor.txt sensor$1_rand-s1.1-dis5.txt
head -n $1 sensor$1_rand-s1.1-dis5.txt > cutted_$1
cp cutted_$1 sensor$1_rand-s1.1-dis5.txt
cp sensor$1_rand-s1.1-dis5.txt ~/Dropbox/journal\ with\ liu/experiment/
