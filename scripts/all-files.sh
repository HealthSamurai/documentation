#!/bin/bash
cd docs && find . -type f | sed 's|^./||' | sort
