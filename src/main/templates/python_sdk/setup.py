from setuptools import setup, find_packages

with open("README.md", "r", encoding="utf-8") as fh:
    long_description = fh.read()

setup(
    name="${packageName}",
    version="${version}",
    author="${author}",
    author_email="${authorEmail}",
    description="Flexmodel Python SDK - 直接访问实体类型，无需通用封装",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="${repositoryUrl}",
    packages=find_packages(where="src"),
    package_dir={"": "src"},
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: Developers",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
        "Programming Language :: Python :: 3.12",
    ],
    python_requires=">=3.8",
    install_requires=[
        "requests>=2.25.0",
        "pydantic>=1.8.0",
    ],
    extras_require={
        "dev": [
            "pytest>=6.0",
            "pytest-asyncio>=0.15.0",
            "black>=21.0",
            "flake8>=3.8",
            "mypy>=0.800",
        ],
    },
    keywords="flexmodel sdk python api client",
    project_urls={
        "Bug Reports": "${bugReportsUrl}",
        "Source": "${repositoryUrl}",
        "Documentation": "${documentationUrl}",
    },
)
