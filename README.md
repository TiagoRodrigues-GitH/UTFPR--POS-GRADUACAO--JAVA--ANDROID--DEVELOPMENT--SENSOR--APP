SensorHub
Aplicativo Android para Coleta e Monitoramento de Dados de Sensores em Campo
📋 Sobre o Projeto
SensorHub é uma aplicação Android offline-first desenvolvida para coleta de dados de sensores em ambientes de campo sem conectividade à internet. 
O aplicativo integra três módulos principais de captura de dados

Sensores IoT: Integração com dispositivos IoT externos

🎯 Características Principais

Arquitetura MVVM com separação clara de responsabilidades
Persistência offline usando Room Database com modo WAL
Processamento assíncrono via ExecutorService e WorkManager
Compatibilidade com API 24+ e dispositivos Nexus 7

🏗️ Arquitetura
SensorHub/

├── data/           # Camada de dados (Entities, DAOs, Database, Repositories)

├── domain/         # Lógica de negócio (Use Cases)

├── hardware/       # Abstração de sensores físicos

├── ui/             # Camada de apresentação (Activities, ViewModels, Adapters)

└── util/           # Classes utilitárias
🛠️ Tecnologias

Linguagem: Java
Banco de Dados: Room Persistence Library
Arquitetura: MVVM com LiveData e ViewModels
UI: ViewBinding, RecyclerView, Material Design
Processamento: ExecutorService, WorkManager
ML: Implementações puras Java


📱 Módulos
1. Sensores IoT
Gerenciamento e coleta de dados de sensores IoT
Visualização em tempo real de leituras


🚀 Status do Projeto
Em desenvolvimento ativo 

👨‍💻 Autor
[Seu Nome]
PÓS-GRADUAÇÃO TECNOLOGIAS JAVA - UTFPR
📧 tiagorodrigues@alunos.utfpr.edu.br
📄 Licença
Este projeto foi desenvolvido como parte de atividades acadêmicas na Universidade Tecnológica Federal do Paraná (UTFPR).

Projeto Acadêmico | UTFPR - Campus Londrina | 2026
