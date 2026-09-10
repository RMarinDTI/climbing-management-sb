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
# WARNING:
# Permanently removes uncommitted changes
# in the working directory.

git restore <file>


### Discard all unstaged changes
#
# WARNING:
# Permanently removes all uncommitted changes
# that are not staged.

git restore .


### Commit staged changes
#
# Creates a new commit containing everything
# currently in the staging area.

git commit -m "your commit message"


### Amend the last commit
#
# Replaces the most recent commit with a new commit.
#
# Useful when you:
#   - made a typo in the commit message
#   - want to improve the commit message
#   - forgot to include a file in the last commit
#
# Example:
#
#   git commit -m "remove unnecesaty comments"
#
# Change the commit message:

git commit --amend -m "docs: clean up comments"


### Check the amended commit

git log -1 --oneline


### Push an amended commit that has NOT been pushed yet
#
# Since the original commit was never pushed,
# a normal push is enough.

git push


### Push an amended commit that was ALREADY pushed
#
# IMPORTANT:
# Amend creates a new commit hash.
#
# If the old commit already exists on the remote,
# the remote history must be updated.
#
# Prefer --force-with-lease over --force.
#
# --force-with-lease protects against overwriting
# remote changes that you do not have locally.

git push --force-with-lease


### Show commit history

git log


### Show compact commit history

git log --oneline


### Show the last 10 commits

git log --oneline -10


### Show commits with branches and graph

git log --oneline --graph --decorate --all


### Show a specific commit

git show <commit-hash>


### Show which files changed in a commit

git show --stat <commit-hash>


### Show changes introduced by a commit

git show <commit-hash>


### Compare working directory with staged version
#
# Shows unstaged changes.

git diff


### Compare staged changes with the last commit
#
# Shows what will be included in the next commit.

git diff --staged


### Compare two commits

git diff <commit1> <commit2>


### Show changes in one specific file

git diff -- <file>


### Show the previous version of a file

git show HEAD:<file>


### Show a file from a specific commit

git show <commit-hash>:<file>


### Push the current branch to the remote

git push


### Push a local branch to origin

git push origin <branch-name>


### Push and set the upstream branch
#
# Useful when pushing a new branch for the first time.

git push -u origin <branch-name>


### Delete a remote branch

git push origin --delete <branch-name>


### Rename the current branch

git branch -M <new-name>


### Create a Git tag

git tag <tag-name>


### Create an annotated Git tag

git tag -a <tag-name> -m "tag message"


### List tags

git tag


### Push a tag to GitHub

git push origin <tag-name>


### Push all tags

git push origin --tags


### Remove a file from Git but keep it locally
#
# Useful for files that should no longer be tracked
# but should remain on your computer.

git rm --cached <file>


### Remove a file from Git and from the working directory
#
# WARNING:
# Deletes the file locally as well.

git rm <file>


### Rename a tracked file

git mv <old-name> <new-name>


### Temporarily save uncommitted changes
#
# Useful when you need to switch branches
# without committing incomplete work.

git stash


### Show stashed changes

git stash list


### Apply the most recent stash
#
# Applies the changes but keeps the stash.

git stash apply


### Apply and remove the most recent stash

git stash pop


### Delete the most recent stash

git stash drop


### Merge another branch into the current branch

git merge <branch-name>


### Abort a merge in progress
#
# Returns the repository to the state before the merge.

git merge --abort


### Revert a commit
#
# Creates a NEW commit that undoes the changes
# introduced by an existing commit.
#
# Recommended when the commit has already been pushed.

git revert <commit-hash>


### Reset to the previous commit
#
# Moves HEAD and the current branch backwards.
#
# --soft:
#   Keeps changes staged.
#
# --mixed:
#   Keeps changes but unstages them.
#
# --hard:
#   Deletes changes.
#
# WARNING:
# Be very careful with reset, especially --hard.

git reset --soft HEAD~1

git reset HEAD~1

git reset --hard HEAD~1


### Check which files are ignored

git status --ignored


### Show Git configuration

git config --list


### Set Git username

git config --global user.name "Your Name"


### Set Git email

git config --global user.email "your@email.com"


### Show a specific Git configuration value

git config user.name

git config user.email


### Git workflow — typical development cycle
#
# 1. Check current state

git status

# 2. Make code changes

# 3. Review changes

git diff

# 4. Stage changes

git add <file>

# 5. Review what will be committed

git diff --staged

# 6. Commit

git commit -m "feat: add course endpoint"

# 7. Push

git push


### Git workflow — multiple files
#
# Check status

git status

# Stage everything

git add -A

# Review staged changes

git diff --staged

# Commit

git commit -m "feat: add MongoDB course search"

# Push

git push


### Git recovery — important distinction
#
# UNSTAGED changes:
#
#   git restore <file>
#
# removes the local changes.
#
#
# STAGED changes:
#
#   git restore --staged <file>
#
# removes the file from staging,
# but keeps the local changes.
#
#
# This distinction is important:
#
#   git restore
#       ↓
#   changes are discarded
#
#   git restore --staged
#       ↓
#   changes remain locally
#   but are removed from staging


### Git commit workflow — staging area
#
# Working directory
#       ↓
#     git add
#       ↓
# Staging area
#       ↓
#    git commit
#       ↓
# Local repository
#       ↓
#     git push
#       ↓
# Remote repository


### HEAD
#
# HEAD points to the commit currently checked out.
#
# Example:
#
#   HEAD
#    ↓
#   e31d417
#
# HEAD~1 = previous commit
# HEAD~2 = two commits before HEAD


### HEAD vs origin/main
#
# HEAD:
#   Your current local commit.
#
# origin/main:
#   The latest commit Git knows about
#   on the remote main branch.
#
# If Git says:
#
#   Your branch is ahead of 'origin/main' by 1 commit.
#
# it means:
#
#   Local main
#       ↓
#   [new commit]
#       ↓
#   origin/main
#
# The local commit has not been pushed yet.


### Check whether local and remote are synchronized

git status


### GitHub workflow
#
# Local development
#       ↓
# git add
#       ↓
# git commit
#       ↓
# git push
#       ↓
# GitHub
#       ↓
# GitHub Actions
#       ↓
# CI
#       ↓
# tests + Maven build
#       ↓
# Docker image
#       ↓
# GHCR
#       ↓
# CD
#       ↓
# deployment


### Senior Backend interview points
#
# Be able to explain:
#
# - What Git is
# - Working directory vs staging area vs repository
# - git add
# - git commit
# - git commit --amend
# - git push
# - git pull
# - git fetch
# - branches
# - merge
# - rebase
# - merge conflicts
# - git revert vs git reset
# - git stash
# - HEAD
# - origin/main
# - remote tracking branches
# - commit hashes
# - tags
# - Git hooks
# - GitHub Actions
# - CI/CD integration


### Most important commands — quick reference

# Check status
git status

# See history
git log --oneline

# See changes
git diff

# Stage a file
git add <file>

# Stage everything
git add -A

# Unstage a file
git restore --staged <file>

# Discard local changes
git restore <file>

# Commit
git commit -m "message"

# Amend the last commit
git commit --amend -m "new message"

# Push
git push

# Pull
git pull

# Fetch
git fetch

# Create branch
git switch -c <branch>

# Switch branch
git switch <branch>

# Show branches
git branch -a

# Show commit
git show <commit>

# Revert commit
git revert <commit>

# Temporarily save changes
git stash

# Show stashes
git stash list