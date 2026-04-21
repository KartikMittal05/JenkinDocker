pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        STACK_NAME = "ecommerce"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                url: 'https://github.com/KartikMittal05/JenkinDocker.git'
            }
        }

        stage('Build Services') {
            steps {
                bat '''
                mvn clean package -DskipTests -f Eureka/pom.xml
                mvn clean package -DskipTests -f ConfigServer/pom.xml
                mvn clean package -DskipTests -f APIGateway/pom.xml

                mvn clean package -DskipTests -f Product/pom.xml
                mvn clean package -DskipTests -f Price/pom.xml
                mvn clean package -DskipTests -f Inventory/pom.xml
                
                mvn clean package -DskipTests -f ProductCatalog/pom.xml

                mvn clean package -DskipTests -f Cart/pom.xml
                mvn clean package -DskipTests -f Recommendation/pom.xml
                '''
            }
        }

        stage('Docker Build') {
            steps {
                bat '''
                docker build -t eureka-service ./Eureka
                docker build -t config-service ./ConfigServer
                docker build -t gateway-service ./APIGateway

                docker build -t product-service ./Product
               docker build -t price-service ./Price
                docker build -t inventory-service ./Inventory
                
 docker build -t catalog-service ./ProductCatalog
                docker build -t cart-service ./Cart
                docker build -t recommendation-service ./Recommendation
                '''
            }
        }

        stage('Init Swarm') {
            steps {
                bat '''
                docker info | findstr "Swarm: active"
                IF %ERRORLEVEL% EQU 0 (
                    echo Swarm already initialized
                ) ELSE (
                    docker swarm init
                )
                '''
            }
        }

        stage('Remove Old Stack') {
            steps {
                bat '''
                docker stack rm %STACK_NAME%
                timeout /t 10
                exit /b 0
                '''
            }
        }

        stage('Deploy to Swarm') {
            steps {
                bat '''
                docker stack deploy -c docker-stack.yml %STACK_NAME%
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                bat '''
                docker service ls
                docker stack services %STACK_NAME%
                '''
            }
        }

        // OPTIONAL (you had earlier)
        stage('Check Logs (Optional)') {
            steps {
                bat '''
                docker service logs %STACK_NAME%_gateway --tail 20
                '''
                bat 'exit /b 0'
            }
        }
    }

    // ✅ THIS WAS MISSING
    post {
        success {
            echo "🚀 Ecommerce Microservices deployed successfully on Docker Swarm!"
        }
        failure {
            echo "❌ Pipeline failed — check Jenkins logs carefully"
        }
    }
}