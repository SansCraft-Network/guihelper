import re
import subprocess

def get_current_w():
    try:
        with open('build.gradle', 'r') as f:
            content = f.read()
            # version projectVersion(project, 2, 0, 0)
            match = re.search(r'version projectVersion\(project,\s*(\d+),', content)
            if match:
                return int(match.group(1))
    except Exception:
        pass
    return 0

def get_minecraft_version():
    try:
        with open('build.gradle', 'r') as f:
            content = f.read()
            # compileOnly 'org.spigotmc:spigot-api:1.21.1-R0.1-SNAPSHOT'
            match = re.search(r"org\.spigotmc:spigot-api:(\d+\.\d+\.?\d*)", content)
            if match:
                return match.group(1)
    except Exception:
        pass
    return "1.21"

def map_mc_to_n(mc_version):
    # User mapping: n=1: 1.21.*, n=2: 26.*, n=3: 27.*
    parts = [int(p) for p in re.findall(r'\d+', mc_version)]

    if not parts:
        return 1

    major = parts[0]
    minor = parts[1] if len(parts) > 1 else 0

    if major == 1:
        if minor == 21: return 1
        if minor == 26: return 2
        if minor == 27: return 3
        if minor > 27: return 3 + (minor - 27)
        return 1
    else:
        if major == 26: return 2
        if major == 27: return 3
        if major > 27: return 3 + (major - 27)
        return 1

def get_latest_tag():
    try:
        # Fetch tags first to ensure we have them
        subprocess.run(["git", "fetch", "--tags"], check=True, capture_output=True)
        tag = subprocess.check_output(["git", "describe", "--tags", "--abbrev=0"], stderr=subprocess.STDOUT).decode().strip()
        return tag
    except subprocess.CalledProcessError:
        return None

def main():
    current_w = get_current_w()
    mc_version = get_minecraft_version()
    current_n = map_mc_to_n(mc_version)

    latest_tag = get_latest_tag()

    if not latest_tag:
        new_version = f"{current_w}.{current_n}.0"
    else:
        # Expecting w.n.x
        match = re.match(r"v?(\d+)\.(\d+)\.(\d+)", latest_tag)
        if match:
            last_w = int(match.group(1))
            last_n = int(match.group(2))
            last_x = int(match.group(3))

            if current_w > last_w or current_n > last_n:
                new_version = f"{current_w}.{current_n}.0"
            elif current_w < last_w or current_n < last_n:
                # This case is unusual but we should probably still follow build.gradle
                # and maybe increment x if it matches the current W.N
                new_version = f"{current_w}.{current_n}.0"
            else:
                new_version = f"{current_w}.{current_n}.{last_x + 1}"
        else:
            # If tag doesn't match format, start from 0
            new_version = f"{current_w}.{current_n}.0"

    print(new_version)

if __name__ == "__main__":
    main()
