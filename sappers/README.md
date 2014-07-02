# sappers

All sappers must conform to the following minimalistic "API":

1. they are contained in a directory named after the sapper
2. there must be a `start` file
   a. it must be executable
   b. it must have a shebang line
   c. it should exit with `0` if starting the sapper was successful
   d. it should exit with a non-zero value if starting the sapper was unsuccessful
3. there must be a `stop` file
   a. it must be executable
   b. it must have a shebang line
   c. it should stop the sapper in a quick and reliable way
