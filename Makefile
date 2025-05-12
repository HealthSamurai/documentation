init:
	mkdir -p .git/hooks
	ln -sf ../../scripts/prepush.sh .git/hooks/pre-push 