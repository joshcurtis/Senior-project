MKLAUNCHER_PID=`ps aux | grep "python" | grep -E "mklauncher" | tr -s " " | cut -d " " -f2`
RESOLVE_PID=`ps aux | grep "python" | grep -E "resolve.py" | tr -s " " | cut -d " " -f2`

if [ "$MKLAUNCHER_PID" != "" ]; then
kill $MKLAUNCHER_PID
fi
sleep 5

if [ "$RESOLVE_PID" != "" ]; then
kill $RESOLVE_PID
fi
exit 0