(defproject org.openvoxproject/http-client "2.1.6-SNAPSHOT"
  :description "HTTP client wrapper"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}

  :min-lein-version "2.9.1"

  :parent-project {:coords [org.openvoxproject/clj-parent "7.5.1"]
                   :inherit [:managed-dependencies]}

  ;; Abort when version ranges or version conflicts are detected in
  ;; dependencies. Also supports :warn to simply emit warnings.
  ;; requires lein 2.2.0+.
  :pedantic? :abort

  :dependencies [[org.clojure/clojure]

                 [org.apache.httpcomponents/httpasyncclient]
                 [prismatic/schema]
                 [commons-io]
                 [io.dropwizard.metrics/metrics-core]

                 [org.openvoxproject/ssl-utils]
                 [org.openvoxproject/i18n]

                 [org.slf4j/jul-to-slf4j]]

  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :jar-exclusions [#".*\.java$"]

  ;; By declaring a classifier here and a corresponding profile below we'll get an additional jar
  ;; during `lein jar` that has all the source code (including the java source). Downstream projects can then
  ;; depend on this source jar using a :classifier in their :dependencies.
  :classifiers [["sources" :sources-jar]]

  :profiles {:provided {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :defaults {:dependencies [[cheshire]
                                       [org.openvoxproject/kitchensink :classifier "test"]
                                       [org.openvoxproject/trapperkeeper]
                                       [org.openvoxproject/trapperkeeper :classifier "test"]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10]
                                       [org.openvoxproject/trapperkeeper-webserver-jetty10 :classifier "test"]
                                       [org.openvoxproject/ring-middleware]]
                        :resource-paths ["dev-resources"]
                        :jvm-opts ["-Djava.util.logging.config.file=dev-resources/logging.properties"]}
             :dev-deps  {:dependencies [[org.bouncycastle/bcpkix-jdk18on]]}
             :dev [:defaults :dev-deps]
             :fips-deps {:dependencies [[org.bouncycastle/bcpkix-fips]
                                        [org.bouncycastle/bc-fips]
                                        [org.bouncycastle/bctls-fips]]
                         ;; this only ensures that we run with the proper profiles
                         ;; during testing. This JVM opt will be set in the puppet module
                         ;; that sets up the JVM classpaths during installation.
                         :jvm-opts ~(let [version (System/getProperty "java.version")
                                          [major minor _] (clojure.string/split version #"\.")
                                          unsupported-ex (ex-info "Unsupported major Java version. Expects 17 or 21."
                                                           {:major major
                                                            :minor minor})]
                                      (condp = (java.lang.Integer/parseInt major)
                                        17 ["-Djava.security.properties==dev-resources/jdk17-fips-security"]
                                        21 ["-Djava.security.properties==dev-resources/jdk21-fips-security"]
                                        (throw unsupported-ex)))}
             :fips [:defaults :fips-deps]
             :sources-jar {:java-source-paths ^:replace []
                           :jar-exclusions ^:replace []
                           :source-paths ^:replace ["src/clj" "src/java"]}}

  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/CLOJARS_USERNAME
                                     :password :env/CLOJARS_PASSWORD
                                     :sign-releases false}]]

  :lein-release {:scm :git
                 :deploy-via :lein-deploy}

  :plugins [[lein-parent "0.3.7"]
            [jonase/eastwood "1.2.2" :exclusions [org.clojure/clojure]]
            [org.openvoxproject/i18n "0.9.3"]]

  :eastwood {:continue-on-exception true
             :exclude-namespaces [;; linting this test throws and exception as test-utils/load-test-config
                                  ;; requires the addition of the config in /testutils, excluding for now
                                  puppetlabs.orchestrator.integration.migration-errors-test
                                  ;; The BoltClient protocol has more than 20 functions and therefore an exception is thrown
                                  ;; when compiling it for linting https://github.com/jonase/eastwood/issues/344
                                  puppetlabs.orchestrator.bolt.client]
             :exclude-linters [:no-ns-form-found :reflection :deprecations]
             :ignored-faults {:def-in-def {puppetlabs.http.client.async-plaintext-test [{:line 278}]}}}
)
