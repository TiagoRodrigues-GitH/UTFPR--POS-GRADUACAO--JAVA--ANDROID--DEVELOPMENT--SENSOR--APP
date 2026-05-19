# SensorHub

Aplicativo Android 
para Coleta e Monitoramento de Dados de Sensores em Campo
<img width="282" height="217" alt="Captura de tela 2026-05-11 142029" src="https://github.com/user-attachments/assets/a6f5f08b-b312-4e7f-b793-2b41dee85df3" />
---

## 📋 Sobre o Projeto

SensorHub é uma aplicação Android offline-first desenvolvida para coleta de dados de sensores em ambientes de campo sem conectividade à internet.

O aplicativo integra três módulos principais de captura de dados:

- **Sensores IoT**: Integração com dispositivos IoT externos

---

## 🎯 Características Principais

- ✅ **Arquitetura MVVM** com separação clara de responsabilidades
- ✅ **Persistência offline** usando Room Database
- ✅ **Processamento assíncrono** via ExecutorService e WorkManager
- ✅ **Compatibilidade** com API 24+ e dispositivos Nexus 7

---

## 🏗️ Arquitetura
 SensorHub/
 
   ├── data/           # Camada de dados (Entities, DAOs, Database, Repositories)
   
   ├── domain/         # Lógica de negócio (Use Cases)
   
   ├── hardware/       # Abstração de sensores físicos
   
   ├── ui/             # Camada de apresentação (Activities, ViewModels, Adapters)
   
   └── util/           # Classes utilitárias

---

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|-----------|------------|
| **Linguagem** | Java |
| **Banco de Dados** | Room Persistence Library |
| **Arquitetura** | MVVM com LiveData e ViewModels |
| **UI** | ViewBinding, RecyclerView, Material Design |
| **Processamento** | ExecutorService, WorkManager |
| **ML** | Implementações puras Java |

---

## 📱 Módulos

### 1️⃣ Sensores IoT
- Gerenciamento e coleta de dados de sensores IoT externos
---

## 🚀 Status do Projeto

**🟢 Em desenvolvimento ativo**

- ✅ Fases 1-5 completas (Entities, DAOs, Database, Repositories, Use Cases, Hardware Layer,Navegação e ViewModels compartilhados)
- 🔄 Fase 6-7  Utilizar sensores IoT e receber dados em tempo real. 

---

## Imagens


<img width="381" height="685" alt="Captura de tela 2026-05-11 142010" src="https://github.com/user-attachments/assets/608e5381-8257-4109-a785-a9044a6884e9" />


<img width="381" height="685" alt="Captura de tela 2026-05-11 140311" src="https://github.com/user-attachments/assets/0940645e-6479-4733-ba53-80a6b85b4733" />



<img width="381" height="685" alt="Captura de tela 2026-05-11 141318" src="https://github.com/user-attachments/assets/23ced3ad-46a0-4433-a638-a85c1c5dad43" />


<img width="381" height="685" alt="Captura de tela 2026-05-11 141129" src="https://github.com/user-attachments/assets/16395710-c5be-44ff-8607-d8a891210e0f" />


## 👨‍💻 Autor

**Tiago Rodrigues**  
Pós-Graduação em Tecnologias Java - UTFPR  
📧 tiagorodrigues@alunos.utfpr.edu.br

---

## 📄 Licença

Este projeto foi desenvolvido como parte de atividades acadêmicas na **Universidade Tecnológica Federal do Paraná (UTFPR)**.

---

<div align="center">

**Projeto Acadêmico** | UTFPR - Campus Londrina | 2026

</div>
