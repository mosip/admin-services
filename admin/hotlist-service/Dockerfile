# Use Eclipse Temurin JRE 11 base image
FROM eclipse-temurin:11-jre

# ---------- Build metadata ----------
ARG SOURCE
ARG COMMIT_HASH
ARG COMMIT_ID
ARG BUILD_TIME
LABEL source=${SOURCE}
LABEL commit_hash=${COMMIT_HASH}
LABEL commit_id=${COMMIT_ID}
LABEL build_time=${BUILD_TIME}

# ---------- Spring / Config Environment Arguments ----------
ARG spring_config_label
ARG active_profile
ARG spring_config_url
ARG artifactory_url
ARG is_glowroot
ARG iam_adapter_url_env

# ---------- Container User Configuration ----------
ARG container_user=mosip
ARG container_user_group=mosip
ARG container_user_uid=1001
ARG container_user_gid=1001

# ---------- Runtime Environment Variables ----------
ENV active_profile_env=${active_profile}
ENV spring_config_label_env=${spring_config_label}
ENV spring_config_url_env=${spring_config_url}
ENV artifactory_url_env=${artifactory_url}
ENV is_glowroot_env=${is_glowroot}
ENV iam_adapter_url_env=${iam_adapter_url_env}

# ---------- Install dependencies & create app user ----------
RUN apt-get update -y || (sed -i 's|deb.debian.org|archive.debian.org|g' /etc/apt/sources.list && apt-get update -y) && \
    apt-get install -y --no-install-recommends unzip wget && \
    rm -rf /var/lib/apt/lists/* && \
    if ! getent group ${container_user_group} >/dev/null; then groupadd -g ${container_user_gid} ${container_user_group}; fi && \
    if ! id -u ${container_user} >/dev/null 2>&1; then useradd -u ${container_user_uid} -g ${container_user_group} -s /bin/sh -m ${container_user}; fi

# ---------- Setup working directory ----------
WORKDIR /home/${container_user}
ENV work_dir=/home/${container_user}

# ---------- Create loader path ----------
ARG loader_path=${work_dir}/additional_jars/
RUN mkdir -p ${loader_path}
ENV loader_path_env=${loader_path}

# ---------- Copy JAR ----------
COPY ./target/hotlist-service-*.jar hotlist-service.jar

# ---------- Set permissions ----------
RUN chown -R ${container_user}:${container_user} /home/${container_user}

# ---------- Switch to non-root user ----------
USER ${container_user_uid}:${container_user_gid}

# ---------- Expose port ----------
EXPOSE 8095

# ---------- Startup command ----------
CMD if [ "$is_glowroot_env" = "present" ]; then \
        echo "Glowroot enabled. Downloading and starting with agent..."; \
        wget -q --show-progress "${artifactory_url_env}/artifactory/libs-release-local/io/mosip/testing/glowroot.zip" -O glowroot.zip && \
        unzip -q glowroot.zip && rm -rf glowroot.zip && \
        wget -q --show-progress "${iam_adapter_url_env}" -O "${loader_path_env}/kernel-auth-adapter.jar" && \
        java -javaagent:glowroot/glowroot.jar \
             -Dloader.path="${loader_path_env}" \
             -Dspring.cloud.config.label="${spring_config_label_env}" \
             -Dspring.profiles.active="${active_profile_env}" \
             -Dspring.cloud.config.uri="${spring_config_url_env}" \
             -jar hotlist-service.jar; \
    else \
        echo "Starting service without Glowroot..."; \
        wget -q --show-progress "${iam_adapter_url_env}" -O "${loader_path_env}/kernel-auth-adapter.jar" && \
        java -Dloader.path="${loader_path_env}" \
             -Dspring.cloud.config.label="${spring_config_label_env}" \
             -Dspring.profiles.active="${active_profile_env}" \
             -Dspring.cloud.config.uri="${spring_config_url_env}" \
             -jar hotlist-service.jar; \
    fi