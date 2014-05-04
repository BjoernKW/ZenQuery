#!/bin/sh

git checkout production
git merge master
git push origin production
git checkout master
