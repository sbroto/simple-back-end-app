#!/bin/sh -x


start=`date +%s`

export JAVA_HOME=${JAVA_HOME_OPENJDK8}
export PATH=${JAVA_HOME}/bin:${PATH}
export http_proxy=http://sjc1intproxy10.crd.ge.com:8080
export https_proxy=$http_proxy

buildNumber=$1
mode=$2
projectPath=$3

tags=$(git describe --tags)
checkTag=$(git describe --exact-match HEAD)
status=1
packageName="0.0.1--1-SNAPSHOT"

cd $projectPath

curl -H 'Authorization: token 06257b3aa41c2d6c92d69adf643dc7b650fd1bbd' -H 'Accept: application/vnd.github.v3.raw' -O -L https://github.build.ge.com/api/v3/repos/GE-Digital-Foundry-Europe/credentials/contents/settings.xml

#in case of commit on master branch, build and publish to artifactory
if [ "$mode" = "artifactory" ]; then
	if [ ${buildNumber} ]; then
	    if [ ${checkTag} ]; then
	        mvn clean install -s settings.xml -Dci.build.number=${buildNumber} -Dci.build.tag=${tags} -Dci.build.mode="RELEASE" && \
	        #mvn -Pclover.report clover2:setup verify clover2:aggregate clover2:clover clover2:check -Dmaven.clover.targetPercentage=80% -Dmaven.clover.failOnViolation=false -s settings.xml && \
		    mvn -s settings.xml deploy -Dci.build.number=${buildNumber} -Dci.build.tag=${tags} -Dci.build.mode="RELEASE" -DskipITs -DskipTests && \
		    status=0
		    packageName="${tags}-${buildNumber}-${RELEASE}"
	    else
	        mvn clean install -s settings.xml -Dci.build.number=${buildNumber} -Dci.build.tag=${tags} && \
	        #mvn -Pclover.report clover2:setup verify clover2:aggregate clover2:clover clover2:check -Dmaven.clover.targetPercentage=80% -Dmaven.clover.failOnViolation=false -s settings.xml && \
		    mvn -s settings.xml deploy -Dci.build.number=${buildNumber} -Dci.build.tag=${tags} -DskipITs -DskipTests && \
		    status=0
		    packageName="${tags}-${buildNumber}-${SNAPSHOT}"
	    fi
	else
	    mvn clean install -s settings.xml -Dci.build.tag=${tags} && \
	    #mvn -Pclover.report clover2:setup verify clover2:aggregate clover2:clover clover2:check -Dmaven.clover.targetPercentage=80% -Dmaven.clover.failOnViolation=false -s settings.xml && \
		mvn -s settings.xml deploy -DskipITs -DskipTests -Dci.build.tag=${tags} && \
		status=0
	fi
fi

#in case of pullrequest, build
if [ "$mode" = "build" ]; then
	    mvn clean install -s settings.xml -Dci.build.tag=${tags} && \
	    #mvn -Pclover.report clover2:setup verify clover2:aggregate clover2:clover clover2:check -Dmaven.clover.targetPercentage=80% -Dmaven.clover.failOnViolation=false -s settings.xml && \
		status=0
fi

echo $packageName
ls -al target
find target -type f -regex ".*$packageName\.jar"

rm -f settings.xml

end=`date +%s`

runtime=$((end-start))

echo "Build time : ${runtime} sec !!!"

exit ${status}
