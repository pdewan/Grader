#include<stdio.h>
#include<stdlib.h>
#include<errno.h>

#ifdef _WIN32
	#define _RUN_OS_WINDOWS
#elif defined(__CYGWIN__)
	#define _RUN_OS_WINDOWS
#elif defined(__linux__)
	#define _RUN_OS_UNIX_LIKE
#elif defined(__APPLE__)
	#define _RUN_OS_UNIX_LIKE
#endif

#ifdef _RUN_OS_WINDOWS
	#include<process.h>
#elif defined(_RUN_OS_UNIX_LIKE)
	#include<sys/types.h>
	#include<sys/wait.h>
	#include<unistd.h>
#endif

int main(int argc, char* argv[]) {
	if (argc < 2) {
		printf("No command given\n");
		return 0;
	}
	#ifdef _RUN_OS_WINDOWS
		_spawnv(_P_WAIT, argv[1], &(argv[1]));
		return errno;
	#elif defined(_RUN_OS_UNIX_LIKE)
		//int out = dup(1);
		//pid_t pid = fork();
		//if (pid == -1) {
		//	printf("Error forking execution\n");
		//} else if (pid == 0) {
		//	dup2(out, 1);
			int ret;
			ret = execv(argv[1], &(argv[1]));
			return ret;
		//} else {
		//	int status;
		//	(void)waitpid(pid, &status, 0);
		//	return status;
		//}
	#else
		printf("Unsupported operating system");
	#endif

	return 0;
}
