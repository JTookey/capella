pipeline {
	agent {
        kubernetes {
            label 'capella-buildtest'
            defaultContainer 'uitests'
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: uitests
    image: eclipsecbijenkins/ui-test-agent:3.29@sha256:b5f847bd86f2761b7c8487e8b97fc5925d9aba6382c88a68fea1aaa01e12db59
    tty: true
    command: [ "uid_entrypoint", "cat" ]
    resources:
      requests:
        memory: "3.5Gi"
        cpu: "1"
      limits:
        memory: "3.5Gi"
        cpu: "1"
    volumeMounts:
    - name: volume-known-hosts
      mountPath: /home/jenkins/.ssh
    - name: tools
      mountPath: /opt/tools
    - name: settings-xml
      mountPath: /home/jenkins/.m2/settings.xml
      subPath: settings.xml
      readOnly: true
    - name: toolchains-xml
      mountPath: /home/jenkins/.m2/toolchains.xml
      subPath: toolchains.xml
      readOnly: true
    - name: settings-security-xml
      mountPath: /home/jenkins/.m2/settings-security.xml
      subPath: settings-security.xml
      readOnly: true
    - name: m2-repo
      mountPath: /home/jenkins/.m2/repository
  volumes:
  - name: volume-known-hosts
    configMap:
      name: known-hosts
  - name: tools
    persistentVolumeClaim:
      claimName: tools-claim-jiro-capella
  - name: settings-xml
    secret:
      secretName: m2-secret-dir
      items:
      - key: settings.xml
        path: settings.xml
  - name: toolchains-xml
    configMap:
      name: m2-dir
      items:
      - key: toolchains.xml
        path: toolchains.xml
  - name: settings-security-xml
    secret:
      secretName: m2-secret-dir
      items:
      - key: settings-security.xml
        path: settings-security.xml
  - name: m2-repo
    emptyDir: {}
"""
        }
    }
  
	tools {
		maven 'apache-maven-latest'
		jdk 'oracle-jdk8-latest'
	}
  
	environment {
		BUILD_KEY = (github.isPullRequest() ? CHANGE_TARGET : BRANCH_NAME).replaceFirst(/^v/, '')
		CAPELLA_PRODUCT_PATH = "${WORKSPACE}/releng/plugins/org.polarsys.capella.rcp.product/target/products/org.polarsys.capella.rcp.product/linux/gtk/x86_64/eclipse"
  	}
  
  	stages {
  	
		stage('Generate Target Platform') {
	    	steps {        
	        	script { 
		        	if(github.isPullRequest()){
		        	    github.buildStartedComment()
		        	}
		
		        	currentBuild.description = BUILD_KEY
		        	
		        	sh 'env'
		        	sh 'mvn clean verify -f releng/plugins/org.polarsys.capella.targets/pom.xml'
	       		}         
	     	}
	    }
	    
    	stage('Build and Package') {
      		steps {
      			script {
      				def customParams = github.isPullRequest() ? '-DSKIP_SONAR=true' : '-Psign'
      	    
      	    		sh "mvn -Djacoco.skip=true -DjavaDocPhase=none -Pfull ${customParams} clean package -f pom.xml"
	       		}         
	     	}
	    }
    
		stage('Deploy to Nightly') {
      		steps {
				script {		
		    		def deploymentDirName = 
		    			(github.isPullRequest() ? "${BUILD_KEY}-${BRANCH_NAME}-${BUILD_ID}" : "${BRANCH_NAME}-${BUILD_ID}")
		    			.replaceAll('/','-')		
		
				    deployer.capellaNightlyProduct("${WORKSPACE}/releng/plugins/org.polarsys.capella.rcp.product/target/products/capella-*.zip", deploymentDirName)
				    
				    deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.test.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.test.site")
				    deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.egf.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.egf.site")
				    deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.rcp.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.rcp.site")
				    deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.targets/full/*", "${deploymentDirName}/targets")
				    
				    currentBuild.description = "${deploymentDirName} - <a href=\"https://ci-staging.eclipse.org/capella/view/Capella/job/capella-product/\">build</a> - <a href=\"https://download.eclipse.org/capella/core/products/nightly/${deploymentDirName}\">product</a>" 				
	       		}         
	     	}
	    }
    
    	stage('Deploy to Nightly Root') {      	
    		when {
	       		expression { 
	        		!github.isPullRequest()
	        	}
      		}
      			
      		steps {      			
      			script {
			        def deploymentDirName = BUILD_KEY		
			
					deployer.cleanCapellaNightlyArtefacts(deploymentDirName)
					
					deployer.capellaNightlyProduct("${WORKSPACE}/releng/plugins/org.polarsys.capella.rcp.product/target/products/capella-*.zip", deploymentDirName)
					    
					deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.test.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.test.site")
					deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.egf.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.egf.site")
					deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.rcp.site/target/repository/**", "${deploymentDirName}/org.polarsys.capella.rcp.site")
					deployer.capellaNightlyUpdateSite("${WORKSPACE}/releng/plugins/org.polarsys.capella.targets/full/*", "${deploymentDirName}/targets")
			      
			      	currentBuild.description = "${BUILD_KEY} - <a href=\"https://ci-staging.eclipse.org/capella/view/Capella/job/capella-product/\">build</a> - <a href=\"https://download.eclipse.org/capella/core/products/nightly/${env.BUILD_KEY}\">product</a>" } 
			}
		}

    	stage('Install test features') {
    		when {
	        	expression { 
	        		github.isPullRequest()
	        	}
	      	}
    	
        	steps {        	
        		script {
	        		sh "chmod 755 ${CAPELLA_PRODUCT_PATH}"
	        		
	        		eclipse.installFeature("${CAPELLA_PRODUCT_PATH}", 'http://download.eclipse.org/tools/orbit/downloads/drops/R20130827064939/repository', 'org.jsoup')	        		
	        		eclipse.installFeature("${CAPELLA_PRODUCT_PATH}", "file:/${WORKSPACE}/releng/plugins/org.polarsys.capella.test.site/target/repository".replace("\\", "/"), 'org.polarsys.capella.test.feature.feature.group')
	       		}         
	     	}
	    }
	    
    	stage('Run tests') {    		
    		when {
	          	expression { 
	        		github.isPullRequest()
	        	}
	      	}
    		
        	steps {              	
        		script {
        			wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
		        		
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'ModelQueriesValidation', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.business.queries.ju.testSuites.main.BusinessQueryTestSuite',
		        			 'org.polarsys.capella.test.semantic.queries.ju.testsuites.SemanticQueriesTestSuite', 
		        			 'org.polarsys.capella.test.validation.rules.ju.testsuites.main.ValidationRulesTestSuite'])
		        			 
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'LibRecTransition', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.libraries.ju.testsuites.main.LibrariesTestSuite',
		        			  'org.polarsys.capella.test.recrpl.ju.testsuites.main.RecRplTestSuite',
		        			  'org.polarsys.capella.test.transition.ju.testsuites.main.TransitionTestSuite'])
		        		
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'DiagramTools1', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.MSDiagramToolsTestSuite', 
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.SFDBDiagramToolsTestSuite', 
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.MSMDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.CDBDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.LABDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.IDBDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.PABDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.SABDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.MCBDiagramToolsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.XABDiagramToolsTestSuite'
		        			])
		        			
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'DiagramTools2', 'org.polarsys.capella.test.suites.ju',
		        			['org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.ESDiagramToolsTestSuite', 
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.XBDiagramToolsTestSuite', 
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.XDFBDiagramToolsTestSuite', 
		        			 'org.polarsys.capella.test.diagram.tools.ju.testsuites.partial.DiagramActionsTestSuite',
		        			 'org.polarsys.capella.test.diagram.tools.ju.es.MultiInstanceRoleTest'
		        			])
		        			        			
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'DiagramMiscFilters', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.diagram.misc.ju.testsuites.DiagramMiscTestSuite',
		        			  'org.polarsys.capella.test.diagram.filters.ju.testsuites.DiagramFiltersTestSuite'])		        			    
		   
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'Views', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.model.ju.testsuites.main.ModelTestSuite', 
		        			 'org.polarsys.capella.test.richtext.ju.testsuites.RichtextTestSuite',
		        			 'org.polarsys.capella.test.fastlinker.ju.testsuites.FastLinkerTestsSuite',
		        			 'org.polarsys.capella.test.explorer.activity.ju.testsuites.ActivityExplorerTestsSuite',
		        			 'org.polarsys.capella.test.progressmonitoring.ju.testsuites.SetProgressTestSuite',
		        			 'org.polarsys.capella.test.navigator.ju.testsuites.main.NavigatorUITestSuite'])
		        			 
		        		tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'MigrationCommandLine', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.migration.ju.testsuites.main.MigrationTestSuite',
		        			 'org.polarsys.capella.test.diagram.layout.ju.testsuites.LayoutTestSuite',
		        			 'org.polarsys.capella.test.commandline.ju.testsuites.CommandLineTestSuite'])		 	
		   
		  				tester.runUITests("${CAPELLA_PRODUCT_PATH}", 'Detach', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.model.ju.testsuites.partial.DetachTestSuite'])
		        			
		        		tester.runNONUITests("${CAPELLA_PRODUCT_PATH}", 'NotUINavigator', 'org.polarsys.capella.test.suites.ju', 
		        			['org.polarsys.capella.test.navigator.ju.testsuites.main.NavigatorTestSuite'])
	        		}
	        		
	        		junit '*.xml'
				}
			}
		}
	}
  
	post {
    	always {
       		archiveArtifacts artifacts: '**/*.log, *.log, *.xml, **/*.layout'
    	}
    	
    	success  {
    		script {
    			if(github.isPullRequest()){
        			github.buildSuccessfullComment()
        		}
        	}
    	}
    	
	    unstable {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildUnstableComment()
	        	}
	        }
	    }
    
	    failure {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildFailedComment()
	        	}
	        }
	    }
	    
	    aborted {
	    	script {
	    		if(github.isPullRequest()){
	        		github.buildAbortedComment()
	        	}
	        }
	    }
	}
}