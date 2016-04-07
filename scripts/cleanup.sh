PIDS=`ps aux | grep "python" | grep -E "resolve.py|mklauncher" | tr -s " " | cut -d " " -f2`
if [ "$PIDS" != "" ]; then
kill $PIDS
fi
sleep 1
exit 0