FROM docker:19.03.1
COPY entrypoint.sh /entrypoint.sh
RUN chmod a+x /entrypoint.sh
RUN apk --update add git
ENTRYPOINT ["/entrypoint.sh"]
