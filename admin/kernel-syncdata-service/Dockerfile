FROM eclipse-temurin:11-jre

ARG SOURCE
ARG COMMIT_HASH
ARG COMMIT_ID
ARG BUILD_TIME
LABEL source=${SOURCE}
LABEL commit_hash=${COMMIT_HASH}
LABEL commit_id=${COMMIT_ID}
LABEL build_time=${BUILD_TIME}

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG spring_config_label

# can be passed during Docker build as build time environment for spring profiles active 
ARG active_profile

# can be passed during Docker build as build time environment for config server URL 
ARG spring_config_url

# can be passed during Docker build as build time environment for glowroot 
ARG is_glowroot

# can be passed during Docker build as build time environment for artifactory URL
ARG artifactory_url
ARG iam_adapter_url

# environment variable to pass active profile such as DEV, QA etc at docker runtime
ENV active_profile_env=${active_profile}

# environment variable to pass github branch to pickup configuration from, at docker runtime
ENV spring_config_label_env=${spring_config_label}

# environment variable to pass spring configuration url, at docker runtime
ENV spring_config_url_env=${spring_config_url}

# environment variable to pass glowroot, at docker runtime
ENV is_glowroot_env=${is_glowroot}

# environment variable to pass artifactory url, at docker runtime
ENV artifactory_url_env=${artifactory_url}
# environment variable to pass iam_adapter url, at docker runtime
ENV iam_adapter_url_env=${iam_adapter_url}

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user=mosip

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_group=mosip

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_uid=1001

# can be passed during Docker build as build time environment for github branch to pickup configuration from.
ARG container_user_gid=1001

# ---------- Install dependencies & create user ----------
RUN apt-get update -y || (sed -i 's|deb.debian.org|archive.debian.org|g' /etc/apt/sources.list && apt-get update -y) && \
    apt-get install -y --no-install-recommends unzip wget && \
    rm -rf /var/lib/apt/lists/* && \
    if ! getent group ${container_user_group} >/dev/null; then groupadd -g ${container_user_gid} ${container_user_group}; fi && \
    if ! id -u ${container_user} >/dev/null 2>&1; then useradd -u ${container_user_uid} -g ${container_user_group} -s /bin/sh -m ${container_user}; fi

# ---------- Setup Working Directory ----------
WORKDIR /home/${container_user}
ENV work_dir=/home/${container_user}

# ---------- Create Loader Path ----------
ARG loader_path=${work_dir}/additional_jars/
RUN mkdir -p ${loader_path}
ENV loader_path_env=${loader_path}

# ---------- Copy JAR ----------
COPY ./target/kernel-syncdata-service-*.jar kernel-syncdata-service.jar

# ---------- Change Permissions ----------
RUN chown -R ${container_user}:${container_user} /home/${container_user}

# ---------- Switch to Non-Root User ----------
USER ${container_user_uid}:${container_user_gid}

# ---------- Expose Port ----------
EXPOSE 8089

# ---------- Main Command ----------
CMD if [ "$is_glowroot_env" = "present" ]; then \
        echo "🔹 Glowroot enabled — downloading agent..."; \
        wget -q --show-progress "${artifactory_url_env}/artifactory/libs-release-local/io/mosip/testing/glowroot.zip" -O glowroot.zip && \
        if unzip -t glowroot.zip >/dev/null 2>&1; then \
            unzip -q glowroot.zip || true; \
            rm -f glowroot.zip; \
            sed -i 's/<service_name>/kernel-syncdata-service/g' glowroot/glowroot.properties 2>/dev/null || true; \
        else \
            echo "⚠️  Glowroot zip invalid or empty, skipping extraction."; \
        fi; \
        echo "🔹 Downloading IAM adapter..."; \
        wget -q --show-progress "${iam_adapter_url_env}" -O "${loader_path_env}/kernel-auth-adapter.jar" || echo "⚠️  Failed to fetch IAM adapter"; \
        echo "🔹 Starting service with Glowroot..."; \
        java -javaagent:glowroot/glowroot.jar \
             -Dloader.path="${loader_path_env}" \
             -Dspring.cloud.config.label="${spring_config_label_env}" \
             -Dspring.profiles.active="${active_profile_env}" \
             -Dspring.cloud.config.uri="${spring_config_url_env}" \
             -jar kernel-syncdata-service.jar; \
    else \
        echo "🔹 Starting service without Glowroot..."; \
        wget -q --show-progress "${iam_adapter_url_env}" -O "${loader_path_env}/kernel-auth-adapter.jar" || echo "⚠️  Failed to fetch IAM adapter"; \
        java -Dloader.path="${loader_path_env}" \
             -Dspring.cloud.config.label="${spring_config_label_env}" \
             -Dspring.profiles.active="${active_profile_env}" \
             -Dspring.cloud.config.uri="${spring_config_url_env}" \
             -jar kernel-syncdata-service.jar; \
    fi
