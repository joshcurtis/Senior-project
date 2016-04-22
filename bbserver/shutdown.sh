MKLAUNCHER_PID=`ps aux | grep "python" | grep -E "mklauncher" | tr -s " " | cut -d " " -f2`

if [ "$MKLAUNCHER_PID" != "" ]; then
kill $MKLAUNCHER_PID
fi
sleep 5
