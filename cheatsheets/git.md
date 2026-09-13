GIT COMMANDS — CLIMBING MANAGEMENT
==================================


### Check Git version

git --version


### Check repository status
#
# Shows:
#   - current branch
#   - commits ahead/behind remote
#   - staged changes
#   - unstaged changes
#   - untracked files

git status


### Initialize a Git repository
#
# Creates a new local Git repository in the current directory.

git init


### Clone a repository
#
# Downloads an existing remote repository
# and creates a local working copy.

git clone <repository-url>


### Show configured remote repositories
#
# Shows where the local repository gets/pushes code.

git remote -v


### Show detailed remote information

git remote show origin


### Add a remote repository

git remote add origin <repository-url>


### Show current branch

git branch --show-current


### List local branches

git branch


### List local and remote branches

git branch -a


### Create a new branch

git branch <branch-name>


### Create and switch to a new branch

git switch -c <branch-name>


### Switch to an existing branch

git switch <branch-name>


### Switch to the previous branch

git switch -


### Delete a local branch
#
# -d only deletes the branch if Git considers it safely merged.

git branch -d <branch-name>


### Force-delete a local branch
#
# WARNING:
# Can delete a branch containing unmerged commits.

git branch -D <branch-name>


### Download information from the remote repository
#
# Updates remote-tracking references.
# Does NOT modify your working files.

git fetch


### Fetch from a specific remote

git fetch origin


### Pull changes from the remote
#
# Fetches remote changes and integrates them
# into the current branch.
#
# By default this is typically:
#
#   git fetch
#   git merge
#
# Depending on configuration, git pull can instead
# perform a rebase.

git pull


### Pull from origin/main

git pull origin main


### Stage one file
#
# Moves the current version of the file
# into the staging area.

git add <file>


### Stage multiple files

git add <file1> <file2> <file3>


### Stage all changes
#
# Stages:
#   - modified files
#   - deleted files
#   - new files

git add -A


### Stage all changes in the current directory

git add .


### Unstage a file
#
# Removes the file from the staging area
# but keeps the changes in your working directory.

git restore --staged <file>


### Discard changes in a file
#
#