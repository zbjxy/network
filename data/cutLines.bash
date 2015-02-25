echo 'head -n' $2 $1 '>' $1
head -n $2 $1 > cutted_$1
cp cutted_$1 $1
