param(
    [string]$JdkHome = ""
)

$ErrorActionPreference = "Stop"
$projectDirectory = $PSScriptRoot
$junitJar = Join-Path $projectDirectory "lib\junit-platform-console-standalone-1.11.4.jar"

if (-not (Test-Path -LiteralPath $junitJar)) {
    throw "Missing JUnit dependency. See Teste/README.md for the download command."
}

if ($JdkHome) {
    $java = Join-Path $JdkHome "bin\java.exe"
    $javac = Join-Path $JdkHome "bin\javac.exe"
} elseif ($env:JAVA_HOME) {
    $java = Join-Path $env:JAVA_HOME "bin\java.exe"
    $javac = Join-Path $env:JAVA_HOME "bin\javac.exe"
} elseif ((Get-Command java -ErrorAction SilentlyContinue) -and
          (Get-Command javac -ErrorAction SilentlyContinue)) {
    $java = (Get-Command java).Source
    $javac = (Get-Command javac).Source
} else {
    $localJdks = Join-Path $env:USERPROFILE ".jdks"
    $jdk = Get-ChildItem -LiteralPath $localJdks -Directory -ErrorAction SilentlyContinue |
        Where-Object { Test-Path -LiteralPath (Join-Path $_.FullName "bin\javac.exe") } |
        Sort-Object LastWriteTime -Descending |
        Select-Object -First 1
    if (-not $jdk) {
        throw "No JDK found. Supply -JdkHome with the path to an installed JDK."
    }
    $java = Join-Path $jdk.FullName "bin\java.exe"
    $javac = Join-Path $jdk.FullName "bin\javac.exe"
}

# A fresh directory prevents removed tests from surviving as stale class files.
$runDirectory = Join-Path $projectDirectory ("out\junit\" + [guid]::NewGuid().ToString())
$productionOutput = Join-Path $runDirectory "production"
$testOutput = Join-Path $runDirectory "tests"
$reportOutput = Join-Path $runDirectory "reports"
New-Item -ItemType Directory -Path $productionOutput,$testOutput,$reportOutput -Force | Out-Null

$productionSources = @(Get-ChildItem (Join-Path $projectDirectory "src") -Recurse -Filter "*.java" |
    Select-Object -ExpandProperty FullName)
$testSources = @(Get-ChildItem (Join-Path $projectDirectory "Teste") -Recurse -Filter "*.java" |
    Select-Object -ExpandProperty FullName)
& $javac -encoding UTF-8 -d $productionOutput @productionSources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& $javac -encoding UTF-8 -cp "$productionOutput;$junitJar" -d $testOutput @testSources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Host "JUnit XML reports: $reportOutput"
& $java -jar $junitJar execute --class-path "$productionOutput;$testOutput" --scan-class-path --fail-if-no-tests --disable-ansi-colors --details summary --reports-dir $reportOutput
exit $LASTEXITCODE
