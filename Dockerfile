FROM continuumio/miniconda3
# keep python from generating .pyc file in container
ENV PYTHONDONTWRITEBYTECODE = 1
# turn off buffering for easier container logging
ENV PYTHONNUNBUFFERED = 1

WORKDIR /app

# create environment:
COPY . /app
RUN conda env create -f environment.yml 


# Make RUN commands use the new environment:
SHELL ["conda", "run", "--no-capture-output", "-n", "myenv", "/bin/bash", "-c"]

EXPOSE 5000
# The code to run when container is started:
ENTRYPOINT ["conda", "run", "--no-capture-output", "-n", "myenv", "python", "server.py"] 